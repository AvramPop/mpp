package ro.ubb.socket.server;



import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.server.infrastructure.TCPServer;
import ro.ubb.socket.server.repository.db.DBStudentRepository;
import ro.ubb.socket.server.service.StudentServerService;
import ro.ubb.socket.server.service.validators.StudentValidator;
import ro.ubb.socket.server.service.validators.Validator;

import java.nio.file.FileSystems;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ServerApp {

    public static void main(String[] args) {
        Validator<Student> studentValidator = new StudentValidator();
        DBStudentRepository studentRepository =
            new DBStudentRepository(
                "configuration" + FileSystems.getDefault().getSeparator() +
                    "db-credentials.data");
        try {
            System.out.println("server started");
            ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
            );
            StudentService studentService = new StudentServerService(studentRepository, studentValidator, executorService);
//            LabProblemService labProblemService = new LabProblemServerService(executorService);
//            AssignmentService assignmentService = new AssignmentServerService(executorService);
            TCPServer tcpServer = new TCPServer(executorService);

            tcpServer.addHandler(MessageHeader.STUDENT_BY_ID, (request) -> {
                Future<Student> future = studentService.getStudentById(Long.parseLong(request.getBody()));
                try {
                    Student result = future.get();
                    return new Message(MessageHeader.OK_REQUEST, StringEntityFactory.entityToMessage(result));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
                }

            });

            tcpServer.addHandler(MessageHeader.STUDENT_ALL, (request) -> {
                Future<Set<Student>> future = studentService.getAllStudents();
                try {
                    Set<Student> result = future.get();
                    return new Message(MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
                }

            });

//        tcpServer.addHandler(HelloService.SAY_BYE, (request) -> {
//            String name = request.getBody();
//            Future<String> future = helloService.sayBye(name);
//            try {
//                String result = future.get();
//                return new Message("ok", result);
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//                return new Message("error", e.getMessage());
//            }
//        });

            tcpServer.startServer();

            executorService.shutdown();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
}
