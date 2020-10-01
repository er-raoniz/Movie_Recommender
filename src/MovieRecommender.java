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

	public static void main (String [] args)
	{
		  try{
		         //creating data model
		         DataModel datamodel = new FileDataModel(new File("../data/moviedata.csv")); //data
		         File file = new File("../data/output-new.txt");
		         file.createNewFile();
		         FileWriter writer = new FileWriter(file); 
		         
		         //Creating UserSimilarity object.
		         UserSimilarity usersimilarity = new PearsonCorrelationSimilarity(datamodel);
		      
		         //UserNeighbourHHood object.
		         UserNeighborhood userneighborhood = new ThresholdUserNeighborhood(0.1, usersimilarity, datamodel);
		      
		         //UserRecommender
		         UserBasedRecommender recommender = new GenericUserBasedRecommender(datamodel, userneighborhood, usersimilarity);
		         
		         //Write recommendations to file
			  for (LongPrimitiveIterator users = datamodel.getUserIDs(); users.hasNext();)
		               {
		                long userId = users.next();
		                List<RecommendedItem> recommendations = recommender.recommend(userId, 10);
						   StringBuilder writeToFile = new StringBuilder(userId + "[");
						   for (RecommendedItem recommendation : recommendations)
		                {
		                	writeToFile.append(recommendation.getItemID()).append(":").append(recommendation.getValue()).append(",");
		                	
		                }
		                writeToFile.append("]").append("\n");
		                writer.write(writeToFile.toString());
		                
		              }
		      writer.flush();
		      writer.close();
		      }
		  catch(Exception e){e.printStackTrace();}
		      
		 }
	}

