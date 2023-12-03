import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import model.Book;
import mongodb.BookManager;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

public class WhenANewBookIsCreated {

    @ClassRule
    public static ManagedMongoDb managedMongoDb = newManagedMongoDbRule().mongodPath("C:\\Program Files\\MongoDB\\Server\\7.0").appendSingleCommandLineArguments("-vvv")
            .build();
    
    
    @Rule
    public MongoDbRule remoteMongoDbRule = newMongoDbRule().defaultManagedMongoDb("test");

    @Test
    @UsingDataSet(locations = "initialData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "expectedData.json")
    public void createANewBook() {

        BookManager bookManager = new BookManager(MongoDbUtil.getCollection(Book.class.getSimpleName()));

        Book book = new Book("The Lord Of The Rings", 1293);
        Book book2 = new Book("The mystery of adult", 299);
        bookManager.create(book);
        bookManager.create(book2);
    }

}
