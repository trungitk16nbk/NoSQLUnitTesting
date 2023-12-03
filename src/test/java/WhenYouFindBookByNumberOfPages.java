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
import java.util.ArrayList;
import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIn.isIn;
import static org.junit.Assert.assertThat;

@UsingDataSet(locations = "initialData.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
public class WhenYouFindBookByNumberOfPages {
    
    @ClassRule
    public static ManagedMongoDb managedMongoDb = newManagedMongoDbRule().mongodPath("C:\\Program Files\\MongoDB\\Server\\7.0").appendSingleCommandLineArguments("-vvv")
            .build();
    
    @Rule
    public MongoDbRule remoteMongoDbRule = newMongoDbRule().defaultManagedMongoDb("test");
    
    @Inject
    private MongoClient mongo;
    
    @Test
    public void findBooksByNumberOfPagesAndHaveResult() {
        
        BookManager bookManager = new BookManager(bookCollection());
        List<Book> books = bookManager.findByNumberOfPages(293);
        
        Book expectedBook1 = new Book("The Hobbit", 293);
        Book expectedBook2 = new Book("Harry Potter and the Philosopher's Stone", 293);
        
        List<Book> expectedBooks = new ArrayList<Book>();
        
        expectedBooks.add(expectedBook1);
        expectedBooks.add(expectedBook2);
        
        assertThat(books, hasSize(2));
        
        for(Book expectedBook : expectedBooks){
    
            assertThat(expectedBook, isIn(books));
            
        }
        
    }
    
    @Test
    public void findBooksByNumberOfPagesAndReturnEmptyResult(){
    
        BookManager bookManager = new BookManager(bookCollection());
        List<Book> books = bookManager.findByNumberOfPages(292);
        
        assertThat(books, hasSize(0));
        
    }
    
    
    private DBCollection bookCollection() {
        return mongo.getDB("test").getCollection(Book.class.getSimpleName());
    }
    
    
    
}
