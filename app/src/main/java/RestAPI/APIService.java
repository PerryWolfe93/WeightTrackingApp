package RestAPI;


import com.fitness_app.object_classes.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @POST("user")
    Call<User> createUser(@Body User user);

}
