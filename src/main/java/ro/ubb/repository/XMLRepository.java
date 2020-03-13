package ro.ubb.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.domain.BaseEntity;
import ro.ubb.domain.ObjectFromFileLine;
import ro.ubb.domain.ObjectFromXMLFile;
import ro.ubb.domain.exceptions.ValidatorException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class XMLRepository<ID, T extends BaseEntity<ID>> implements Repository<ID, T> {

    private String filename;
    private ObjectFromXMLFile<T> converterFunction;

    public XMLRepository(String filename, ObjectFromXMLFile<T> converterFunction) {
        this.filename = filename;
        this.converterFunction = converterFunction;

    }


    /**
     * Find the entity with the given {@code id}.
     *
     * @param id must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<T> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        Map<ID, T> entities =
                StreamSupport.stream(loadData().spliterator(), false)
                        .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));

        return Optional.ofNullable(entities.get(id));
    }

    /** @return all entities. */
    @Override
    public Iterable<T> findAll() {
        return StreamSupport.stream(loadData().spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Saves the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists)
     *     returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException if the entity is not valid.
     */
    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        Map<ID, T> entities =
                StreamSupport.stream(loadData().spliterator(), false)
                        .collect(Collectors.toMap(BaseEntity::getId, elem -> elem));
        Optional<T> optional = Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));

        if (optional.isPresent()) return optional;

        saveToFile(entity);
        return Optional.empty();
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the
     *     removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<T> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        Map<ID, T> entities =
                StreamSupport.stream(loadData().spliterator(), false)
                        .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));
        Optional<T> optional = Optional.ofNullable(entities.remove(id));

        if (optional.isEmpty()) return Optional.empty();

        saveToFile(entities.values());
        return optional;
    }

    /**
     * Updates the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist)
     *     returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException if the entity is not valid.
     */
    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }


        Map<ID, T> entities =
                StreamSupport.stream(loadData().spliterator(), false)
                        .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));

        Optional<T> optional =
                Optional.ofNullable(
                        entities.computeIfPresent(entity.getId(), (k, v) -> entity) == null ? entity : null);
        if (optional.isPresent()) return optional;

        saveToFile(entities.values());
        return Optional.empty();
    }

    private Iterable<T> loadData(){

        HashSet<T> newEntities = new HashSet<>();

        try {
            Document xmlFileDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
            Element root = xmlFileDocument.getDocumentElement();
            NodeList childrenNodes = root.getChildNodes();

            return IntStream.range(0,childrenNodes.getLength()).
                    mapToObj(index -> childrenNodes.item(index)).
                    filter(node ->node instanceof Element).
                    map(node -> converterFunction.convert((Element)node)).collect(Collectors.toSet());

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return newEntities;
    }

    private void saveToFile(Collection<T> entitiesToSave) {
        try {
            Document xmlFileDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
            Element root = xmlFileDocument.getDocumentElement();
            entitiesToSave.forEach(entity -> root.appendChild(entity.objectToXMLNode(xmlFileDocument)));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.transform(new DOMSource(xmlFileDocument),new StreamResult(new File(filename)));
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private void saveToFile(T entity) {
        try {
            Document xmlFileDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
            Element root = xmlFileDocument.getDocumentElement();
            Node entityNode = entity.objectToXMLNode(xmlFileDocument);
            root.appendChild(entityNode);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.transform(new DOMSource(xmlFileDocument),new StreamResult(new File(filename)));
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}