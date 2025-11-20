package com.example.androidapplication.api;

import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.auth.*;
import com.example.androidapplication.data.model.cart.*;
import com.example.androidapplication.data.model.order.*;
import com.example.androidapplication.data.model.product.*;
import com.example.androidapplication.data.model.user.*;

import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // Auth Controller
    @POST("/api/v1/auth/login")
    Call<ApiResponse<InformationDTO>> login(@Body LoginDTO loginDTO);

    @POST("/api/v1/auth/register")
    Call<ApiResponse<InformationDTO>> register(@Body RegisterDTO registerDTO);

    @GET("/api/v1/auth/oauth2/code/google")
    Call<ApiResponse<InformationDTO>> loginWithGoogle(@Query("code") String code);
    @POST("/api/v1/auth/google-signin")
    Call<ApiResponse<InformationDTO>> loginWithGoogleIdToken(@Body Map<String, String> payload);

    @POST("/api/v1/auth/forgot-password")
    Call<ApiResponse<Map<String, String>>> forgotPassword(@Query("email") String email);

    @POST("/api/v1/auth/verify-otp")
    Call<ApiResponse<Map<String, String>>> verifyOtp(@Query("email") String email, @Query("OTP") String otp, @Query("action") String action);

    @POST("/api/v1/auth/reset-password")
    Call<ApiResponse<Object>> resetPassword(@Body ResetPasswordDTO resetPasswordDTO);

    // Product Controller
    @GET("/api/v1/products/home") // THÊM PHƯƠNG THỨC NÀY
    Call<ApiResponse<Map<String, List<Product>>>> getHomePageData();
    // THÊM PHƯƠNG THỨC NÀY
    @GET("/api/v1/products/filter-options")
    Call<ApiResponse<Map<String, List<String>>>> getFilterOptions();

    @GET("/api/v1/products")
    Call<ApiResponse<List<Product>>> getAllProducts();

    @GET("/api/v1/products/filter")
    Call<ApiResponse<ProductFilterResponse>> filterProducts( // SỬA Ở ĐÂY: Map<String, Object> -> ProductFilterResponse
                                                             @Query("page") String page,
                                                             @Query("factory") List<String> factory,
                                                             @Query("target") List<String> target,
                                                             @Query("price") List<String> price,
                                                             @Query("sort") String sort
    );

    @GET("/api/v1/products/{id}")
    Call<ApiResponse<ProductDetail>> getProductDetail(@Path("id") Long id);

    // Cart Controller
    @GET("/api/v1/cart")
    Call<ApiResponse<Cart>> getCart();

    @GET("/api/v1/cart/summary")
    Call<ApiResponse<Map<String, Integer>>> getCartSummary();

    @PATCH("/api/v1/cart/items/{id}")
    Call<ApiResponse<Object>> addProductToCart(@Path("id") Long productId);

    @DELETE("/api/v1/cart/items/{id}")
    Call<ApiResponse<Object>> deleteProductFromCart(@Path("id") Long productId);


    @DELETE("/api/v1/cart/item/{id}")
    Call<ApiResponse<Object>> deleteOneProductFromCart(@Path("id") Long productId);

    @POST("/api/v1/cart/items")
    Call<ApiResponse<Object>> addProductDetailToCart(@Query("id") Long id, @Query("quantity") Long quantity);

    // Order Controller
    @POST("/api/v1/orders/confirm-checkout")
    Call<ApiResponse<Checkout>> confirmCheckout(@Body CartDetailsListDTO cartDetailsListDTO);

    @GET("/api/v1/orders/history")
    Call<ApiResponse<List<Order>>> getOrderHistory();

    // Payment Controller
    @POST("/api/v1/payment")
    Call<ApiResponse<Map<String, String>>> processPayment(@Body InfoOrderRqDTO infoOrderRqDTO);

    // User Controller
    @GET("/api/v1/users/profile")
    Call<ApiResponse<UserProfileUpdateDTO>> getUserProfile();

    @POST("/api/v1/users/profile")
    Call<ApiResponse<Object>> updateUserProfile(@Body UserProfileUpdateDTO profile);

    @PATCH("/api/v1/users/profile/change-password")
    Call<ApiResponse<Object>> changePassword(@Body ChangePasswordDTO changePasswordDTO);

    @Multipart
    @PATCH("/api/v1/users/profile/avatar")
    Call<ApiResponse<Object>> updateAvatar(@Part MultipartBody.Part avatarFile);
}