import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import model.Book;
import mongodb.BookManager;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIn.isIn;
import static org.junit.Assert.assertThat;

public class WhenYouFindIndexedBooks {
    
    @ClassRule
    public static ManagedMongoDb managedMongoDb = newManagedMongoDbRule().mongodPath("C:\\Program Files\\MongoDB\\Server\\7.0").appendSingleCommandLineArguments("-vvv")
            .build();

    @Rule
    public MongoDbRule remoteMongoDbRule = newMongoDbRule().defaultManagedMongoDb("test");

    @Inject
    private MongoClient mongo;

    @Test
    @UsingDataSet(locations = "initialDataIndex.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void findAllBooksByIndex() {

        BookManager bookManager = new BookManager(bookCollection());
        List<Book> books = bookManager.findAll();

        Book expectedBook = new Book("The Hobbit", 293);

        assertThat(books, hasSize(1));
        assertThat(expectedBook, isIn(books));

    }

    private DBCollection bookCollection() {
        return mongo.getDB("test").getCollection(Book.class.getSimpleName());
    }

}
