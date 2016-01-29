package rs.direktnoizbaste.dizb.callback_interfaces;

/**
 * Created by 1 on 1/29/2016.
 */
public interface WebRequestCallbackInterface {
    void webRequestSuccess(boolean success);
    void webRequestError(String error);
}
