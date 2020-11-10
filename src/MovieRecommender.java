import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MovieRecommender {

    public static void main(String[] args) {
        try {
            //creating data model
            DataModel datamodel = new FileDataModel(new File("data/moviedata.csv")); //data
            File user_file = new File("data/user-based.txt");
            user_file.createNewFile();
            FileWriter user_writer = new FileWriter(user_file);

            File item_file = new File("data/item-based.txt");
            item_file.createNewFile();
            FileWriter item_wirter = new FileWriter(item_file);

            //Creating UserSimilarity object.
            UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);

            //UserNeighbourHHood object.
            UserNeighborhood userneighborhood = new ThresholdUserNeighborhood(0.1, usersimilarity, datamodel);

            //UserRecommender
            UserBasedRecommender recommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);

            //Write recommendations to file
            for (LongPrimitiveIterator users = datamodel.getUserIDs(); users.hasNext(); ) {
                long userId = users.next();
                List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
                StringBuilder writeToFile = new StringBuilder(userId + "[");
                for (RecommendedItem recommendation : recommendations) {
                    writeToFile.append(recommendation.getItemID()).append(":").append(recommendation.getValue()).append(",");
                }
                writeToFile.append("]").append("\n");
                user_writer.write(writeToFile.toString());

            }
            user_writer.flush();
            user_writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

