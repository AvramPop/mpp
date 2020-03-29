package ro.ubb.socket.client.service;

import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.exceptions.BadRequestException;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class LabProblemClientService implements LabProblemService {
  private ExecutorService executorService;
  private TCPClient tcpClient;
  //private Validator<LabProblem> validator;
  public LabProblemClientService(ExecutorService executorService, TCPClient tcpClient) {
    this.executorService = executorService;
    this.tcpClient = tcpClient;
    //this.validator = validator;
  }

  @Override
  public Future<Boolean> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);


    return executorService.submit(
        () -> {
          Message request =
              new Message(MessageHeader.LABPROBLEM_ADD, newLabProblem.objectToFileLine());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException("Addition failed, entity already in repository");

          return true;
        });
  }

  @Override
  public Future<Set<LabProblem>> getAllLabProblems() {

    return executorService.submit(()->{
              Message request = new Message(MessageHeader.LABPROBLEM_ALL,"");
              Message response = tcpClient.sendAndReceive(request);
              if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
                throw new BadRequestException(response.getBody());

              String[] lines = response.getBody().split(System.lineSeparator());
              return Arrays.stream(lines).map(StringEntityFactory::labProblemFromMessageLine).collect(Collectors.toSet());

            }
    );

  }

  @Override
  public Future<List<LabProblem>> getAllLabProblemsSorted(Sort sort) {

    return executorService.submit(()->{

              String sortOrder = sort.getSortingChain().stream().map(component -> component.getKey().name() + " " + component.getValue()).
                      reduce("",(partialString, string) -> partialString + string + System.lineSeparator());
              Message request = new Message(MessageHeader.LABPROBLEM_SORTED,sortOrder);
              Message response = tcpClient.sendAndReceive(request);
              if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
                throw new BadRequestException(response.getBody());

              String[] lines = response.getBody().split(System.lineSeparator());
              return Arrays.stream(lines).map(StringEntityFactory::labProblemFromMessageLine).collect(Collectors.toList());

            }
    );

  }

  @Override
  public Future<LabProblem> getLabProblemById(Long id) {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.LABPROBLEM_BY_ID,id.toString());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return StringEntityFactory.labProblemFromMessageLine(response.getBody());
    });

  }

  @Override
  public Future<Boolean> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newStudent = new LabProblem(problemNumber, description);
    newStudent.setId(id);

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.LABPROBLEM_UPDATE,newStudent.objectToFileLine());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return true;
    });
  }

  @Override
  public Future<Boolean> deleteLabProblem(Long id){ // this is not needed here but necessary for interface contract
      return executorService.submit(()->{
          Message request = new Message(MessageHeader.LABPROBLEM_DELETE,id.toString());
          Message response = tcpClient.sendAndReceive(request);
          if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
              throw new BadRequestException(response.getBody());
          return true;
      });
  }
}
