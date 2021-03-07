import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.objects.likes.responses.DeleteResponse;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import com.vk.api.sdk.queries.likes.LikesAddQuery;
import com.vk.api.sdk.queries.likes.LikesDeleteQuery;
import com.vk.api.sdk.queries.likes.LikesGetListQuery;
import com.vk.api.sdk.queries.likes.LikesIsLikedQuery;
import helpers.LikesHepler;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.ConfigManager;
import utils.ResponseObject;

public class LikeTest {
    @DataProvider(name="NegateItemProvider")
    public Object[][] getDataFromDataProvider() {
        return new Object[][] {
                { -1 }
        };
    }

    public static void main(String[] args) {
        new LikeTest().deleteLikeTest();
    }

    @Test(groups = "Positive")
    public void isLikedTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        int itemId = 576;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesIsLikedQuery query = vk.likes().isLiked(actor, type, itemId).ownerId(userId);

        LikesHepler hepler = new LikesHepler();
        ResponseObject<IsLikedResponse, String> responseObject = hepler.executeIsLIkeQuery(query);

        IsLikedResponse response = responseObject.response;
        Assert.assertNotNull(response, String.format("There is no response. %s", responseObject.errorMessage));
        Assert.assertTrue(
                response.isLiked(),
                "There should be like but it wasn't");
    }

    @Test(groups = "Positive")
    public void addLikeTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        int itemId = 575;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesAddQuery query = vk.likes().add(actor, type, itemId).ownerId(userId);

        LikesHepler hepler = new LikesHepler();
        ResponseObject<AddResponse, String> responseObject = hepler.executeAddLikeQuery(query);

        AddResponse response = responseObject.response;
        Assert.assertNotNull(response, String.format("There is no response. %s", responseObject.errorMessage));
        Assert.assertEquals(
                response.getLikes().intValue(),
                1,
                "Like was not added");
    }

    @Test(groups = "Positive")
    public void deleteLikeTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        int itemId = 436;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesAddQuery addQuery = vk.likes().add(actor, type, itemId).ownerId(userId);
        LikesDeleteQuery deleteQuery = vk.likes().delete(actor, type, itemId).ownerId(userId);

        LikesHepler helper = new LikesHepler();
        helper.executeAddLikeQuery(addQuery);
        ResponseObject<DeleteResponse, String> responseObject = helper.executeDeleteLikeQuery(deleteQuery);

        DeleteResponse response = responseObject.response;
        Assert.assertNotNull(response, String.format("There is no response. %s", responseObject.errorMessage));
        Assert.assertEquals(
                response.getLikes().intValue(),
                0,
                "Like was not deleted");
    }

    @Test(groups = "Positive")
    public void getLikesTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        int itemId = 457;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesGetListQuery query = vk.likes().getList(actor, type).ownerId(userId).itemId(itemId);

        LikesHepler helper = new LikesHepler();
        ResponseObject<GetListResponse, String> responseObject = helper.executeGetLikesQuery(query);

        GetListResponse response = responseObject.response;
        Assert.assertNotNull(response, String.format("There is no response. %s", responseObject.errorMessage));
        Assert.assertEquals(
                response.getCount().intValue(),
                3,
                "Incorrect count of likes");
    }

    @Test(groups = "Negative", dataProvider = "NegateItemProvider")
    public void isLikedNegativeItemIdTest(int negativeItemId) {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesAddQuery query = vk.likes().add(actor, type, negativeItemId).ownerId(userId);

        LikesHepler helper = new LikesHepler();
        ResponseObject<AddResponse, String> responseObject = helper.executeAddLikeQuery(query);

        String expectedMessage = "One of the parameters specified was missing or invalid (100): " +
                "One of the parameters specified was missing or invalid: item_id should be positive";
        String errorMessage = responseObject.errorMessage;
        Assert.assertEquals(
                errorMessage,
                expectedMessage,
                "Invalid error message for request which get like with negative item_id");
    }

    @Test(groups = "Negative")
    public void addLikeEmptyTokenTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        int userId = 27614621;
        int itemId = 576;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, "");
        LikesAddQuery query = vk.likes().add(actor, type, itemId).ownerId(userId);

        LikesHepler helper = new LikesHepler();
        ResponseObject<AddResponse, String> responseObject = helper.executeAddLikeQuery(query);

        String errorMessage = responseObject.errorMessage;
        String expectedError = "User authorization failed (5): User authorization failed: no access_token passed.";
        Assert.assertEquals(
                errorMessage,
                expectedError,
                "Invalid error message during adding like with empty access token");
    }

    @Test(groups = "Negative")
    public void deleteLikeNonExistingLikeTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int userId = config.getUserId();
        int itemId = 369;
        Type type = Type.POST;
        UserActor actor = new UserActor(userId, config.getAccessToken());
        LikesDeleteQuery query = vk.likes().delete(actor, type, itemId).ownerId(userId);

        LikesHepler helper = new LikesHepler();
        ResponseObject<DeleteResponse, String> responseObject = helper.executeDeleteLikeQuery(query);

        String errorMessage = responseObject.errorMessage;
        String expectedError = "Access denied (15): Access denied";
        Assert.assertEquals(
                errorMessage,
                expectedError,
                "Invalid error message during deleting non-existing like");
    }

    @Test(groups = "Negative")
    public void getLikesRestrictedPhotoTest() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        ConfigManager config = ConfigManager.getInstance();
        int currentUserId = config.getUserId();
        int userIdWithRestrictedPhoto = config.getUserIdWithRestrictedPhoto();
        int itemId = 457239026;
        Type type = Type.PHOTO;
        UserActor actor = new UserActor(currentUserId, config.getAccessToken());
        LikesGetListQuery query = vk.likes().getList(actor, type).itemId(itemId).ownerId(userIdWithRestrictedPhoto);

        LikesHepler helper = new LikesHepler();
        ResponseObject<GetListResponse, String> responseObject = helper.executeGetLikesQuery(query);

        String errorMessage = responseObject.errorMessage;
        String expectedError = "Access denied (15): Access denied: access restriction";
        Assert.assertEquals(
                errorMessage,
                expectedError,
                "Invalid error message during requesting restricted photo");
    }
}
