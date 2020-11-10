import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MovieRecommender {

    public static void main(String[] args) {
        try {
            //creating data model
            DataModel datamodel = new FileDataModel(new File("data/moviedata.csv")); //data

            // User-based Recommender
            File user_file = new File("data/user-based.txt");
            user_file.createNewFile();
            FileWriter user_writer = new FileWriter(user_file);

            // Creating UserSimilarity object.
            UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);
            // User Neighbourhood object.
            UserNeighborhood userneighborhood = new ThresholdUserNeighborhood(0.1, usersimilarity, datamodel);
            // User Recommender
            UserBasedRecommender userRecommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);

            // Item-based Recommender
            File item_file = new File("data/item-based.txt");
            item_file.createNewFile();
            FileWriter item_writer = new FileWriter(item_file);

            // Creating item similarity object
            ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(datamodel);
            // Item Recommender
            Recommender itemRecommender = new GenericItemBasedRecommender(datamodel, itemSimilarity);

            //Write recommendations to file
            for (LongPrimitiveIterator users = datamodel.getUserIDs(); users.hasNext(); ) {
                long userId = users.next();
                List<RecommendedItem> userRecommendations = userRecommender.recommend(userId, 10);
                StringBuilder writeToUserFile = new StringBuilder(userId + "[");
                for (RecommendedItem recommendation : userRecommendations) {
                    writeToUserFile.append(recommendation.getItemID()).append(":").append(recommendation.getValue()).append(",");
                }
                writeToUserFile.append("]").append("\n");
                user_writer.write(writeToUserFile.toString());

                List<RecommendedItem> itemRecommendations = itemRecommender.recommend(userId, 10);
                StringBuilder writeToItemFile = new StringBuilder(userId + "[");
                for (RecommendedItem recommendation : itemRecommendations) {
                    writeToItemFile.append(recommendation.getItemID()).append(":").append(recommendation.getValue()).append(",");
                }
                writeToItemFile.append("]").append("\n");
                item_writer.write(writeToItemFile.toString());
            }
            user_writer.flush();
            user_writer.close();

            item_writer.flush();
            item_writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

