package helpers;

import com.vk.api.sdk.client.AbstractQueryBuilder;

import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.objects.likes.responses.DeleteResponse;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import com.vk.api.sdk.queries.likes.LikesAddQuery;
import com.vk.api.sdk.queries.likes.LikesDeleteQuery;
import com.vk.api.sdk.queries.likes.LikesGetListQuery;
import com.vk.api.sdk.queries.likes.LikesIsLikedQuery;
import utils.Log;
import utils.ResponseObject;

public class LikesHepler {

    public ResponseObject<IsLikedResponse, String> executeIsLIkeQuery(LikesIsLikedQuery query) {

        logPerformingRequest(query);
        IsLikedResponse response = null;
        try {
            response = query.execute();
        } catch (Exception e) {
            return new ResponseObject(null, e.getMessage());
        }
        logSuccessfullyFinishedRequest(response.toPrettyString());
        return new ResponseObject(response, "");
    }

    public ResponseObject<AddResponse, String> executeAddLikeQuery(LikesAddQuery query) {

        logPerformingRequest(query);
        AddResponse response = null;
        try {
            response = query.execute();
        } catch (Exception e) {
            return new ResponseObject(null, e.getMessage());
        }
        logSuccessfullyFinishedRequest(response.toPrettyString());
        return new ResponseObject(response, "");
    }

    public ResponseObject<DeleteResponse, String> executeDeleteLikeQuery(LikesDeleteQuery query) {

        logPerformingRequest(query);
        DeleteResponse response = null;
        try {
            response = query.execute();
        } catch (Exception e) {
            return new ResponseObject(null, e.getMessage());
        }
        logSuccessfullyFinishedRequest(response.toPrettyString());
        return new ResponseObject<>(response, "");
    }

    public ResponseObject<GetListResponse, String> executeGetLikesQuery(LikesGetListQuery query) {

        logPerformingRequest(query);
        GetListResponse response = null;
        try {
            response = query.execute();
        } catch (Exception e) {
            return new ResponseObject(null, e.getMessage());
        }
        logSuccessfullyFinishedRequest(response.toPrettyString());
        return new ResponseObject(response, "");
    }

    private <T,R> void logPerformingRequest(AbstractQueryBuilder<T, R> queryBuilder) {
        Log.info(String.format(
                "Performing '%s' request with parameters: %s...",
                queryBuilder.getMethod(),
                queryBuilder.build().toString()));
    }

    private void logSuccessfullyFinishedRequest(String responsePrettyStrong) {
        Log.info( String.format( "Request finished successfully: %s", responsePrettyStrong));
    }
}
