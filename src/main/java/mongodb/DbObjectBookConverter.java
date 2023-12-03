package mongodb;

import ch.lambdaj.function.convert.Converter;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static mongodb.MongoDbBookConverter.NUM_PAGES_FIELD;
import static mongodb.MongoDbBookConverter.TITLE_FIELD;

public class DbObjectBookConverter implements
        Converter<DBObject, Book> {

    @Override
    public Book convert(DBObject dbObject) {

        String title = (String) dbObject.get(TITLE_FIELD);
        int numberOfPages = (Integer) dbObject.get(NUM_PAGES_FIELD);

        return new Book(title, numberOfPages);

    }

    public static class BookManager {

        private static final Logger LOGGER = LoggerFactory.getLogger(BookManager.class);

        private static final MongoDbBookConverter MONGO_DB_BOOK_CONVERTER = new MongoDbBookConverter();
        private static final DbObjectBookConverter DB_OBJECT_BOOK_CONVERTER = new DbObjectBookConverter();

        private DBCollection booksCollection;

        public BookManager(DBCollection booksCollection) {
            this.booksCollection = booksCollection;
        }

        public void create(Book book) {
            DBObject dbObject = MONGO_DB_BOOK_CONVERTER.convert(book);
            booksCollection.insert(dbObject);
        }

        public List<Book> findAll() {

            LOGGER.debug("Finding All Elements of Collection " + booksCollection.getName());

            List<Book> books = new ArrayList<Book>();
            DBCursor findAll = booksCollection.find();

            while (findAll.hasNext()) {
                books.add(DB_OBJECT_BOOK_CONVERTER.convert(findAll.next()));
            }

            return books;
        }

        public List<Book> findByNumberOfPages(int number) {

            LOGGER.debug("Finding list of Elements of Collection " + booksCollection.getName());

            BasicDBObject whereQuery = new BasicDBObject();

            whereQuery.put("numberOfPages", number);

            List<Book> books = new ArrayList<Book>();

            DBCursor findByNumberOfPages = booksCollection.find(whereQuery);

            while (findByNumberOfPages.hasNext()) {
                books.add(DB_OBJECT_BOOK_CONVERTER.convert(findByNumberOfPages.next()));
            }

            return books;

        }

        public void deleteByTitle(String title) {

            LOGGER.debug("Deleting a Element of Collection " + booksCollection.getName());

            BasicDBObject whereQuery = new BasicDBObject();

            whereQuery.put("title", title);

            booksCollection.remove(whereQuery);

        }

        public void updateTitle(String oldTitle, String newTitle) {
            LOGGER.debug("Updating a Element of Collection " + booksCollection.getName());

            BasicDBObject whereQuery = new BasicDBObject();
            BasicDBObject newDocument = new BasicDBObject();

            whereQuery.put("title", oldTitle);
            newDocument.put("$set", new BasicDBObject().append("title", newTitle));

            booksCollection.update(whereQuery, newDocument);
        }

        public void updateNumberOfPages(String title, int number) {
            LOGGER.debug("Updating a Element of Collection " + booksCollection.getName());

            BasicDBObject whereQuery = new BasicDBObject();
            BasicDBObject newDocument = new BasicDBObject();

            whereQuery.put("title", title);
            newDocument.put("$set", new BasicDBObject().append("numberOfPages", number));

            booksCollection.update(whereQuery, newDocument);
        }

    }
}
