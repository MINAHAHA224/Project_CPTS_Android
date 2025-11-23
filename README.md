# 💻 3TLap - Modern E-Commerce Android App

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=java)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

<p align="center">
  <img src="app/src/main/res/drawable/logo.png" alt="App Logo" width="150"/>
  <br>
  <i>"Thế giới Laptop chính hãng trong tầm tay bạn."</i>
</p>

---

## 📖 Introduction (Giới thiệu)

**3TLap** là một ứng dụng thương mại điện tử chuyên bán Laptop, được xây dựng trên nền tảng **Android (Java)**. 

Dự án này tập trung vào việc **tối ưu hóa trải nghiệm người dùng (UX/UI)** với giao diện phẳng, hiện đại (Flat Design), đồng thời chứng minh khả năng sử dụng thuần thục các **thành phần cơ bản (Fundamental Components)** của Android như `ListView`, `GridView`, `BaseAdapter` thay vì phụ thuộc vào các thư viện phức tạp.

## ✨ Key Features (Tính năng nổi bật)

### 🔐 Authentication & Security
*   **Đăng nhập / Đăng ký:** Giao diện Gradient hiện đại, hỗ trợ validate dữ liệu.
*   **Google Sign-In:** Tích hợp đăng nhập nhanh bằng tài khoản Google.
*   **Quên mật khẩu (OTP):** Quy trình xác thực email và đặt lại mật khẩu an toàn.
*   **JWT Handling:** Tự động xử lý Token hết hạn (Auto Logout) thông qua `Interceptor`.

### 🛍️ Shopping Experience
*   **Trang chủ (Home):** Banner quảng cáo, Sản phẩm nổi bật (Horizontal Scroll), Sản phẩm mới (Grid View).
*   **Tìm kiếm & Lọc (Search & Filter):** 
    *   Tìm kiếm theo từ khóa Real-time.
    *   Bộ lọc đa tiêu chí: Hãng, Khoảng giá, Sắp xếp (Dialog UI).
*   **Chi tiết sản phẩm:** Xem ảnh, thông số kỹ thuật, mô tả chi tiết, chọn số lượng.

### 🛒 Cart & Checkout
*   **Giỏ hàng thông minh:** Tự động cập nhật tổng tiền, thêm/bớt/xóa sản phẩm mượt mà.
*   **Thanh toán:**
    *   COD (Thanh toán khi nhận hàng).
    *   **Momo Payment:** Tích hợp cổng thanh toán ví điện tử Momo.
*   **Thông báo:** Màn hình "Đặt hàng thành công" chuyên nghiệp.

### 👤 User Profile
*   **Quản lý thông tin:** Cập nhật Avatar, thông tin cá nhân.
*   **Lịch sử đơn hàng:** Xem danh sách đơn hàng, trạng thái vận chuyển, chi tiết từng món hàng (Expandable List).

---

## 📸 Screenshots (Giao diện)

| Login & Auth | Home & Search | Filter & Product |
|:---:|:---:|:---:|
| <img src="screenshots/login.png" width="200"/> | <img src="screenshots/home.png" width="200"/> | <img src="screenshots/filter.png" width="200"/> |

| Cart & Checkout | Order Success | Profile & History |
|:---:|:---:|:---:|
| <img src="screenshots/cart.png" width="200"/> | <img src="screenshots/success.png" width="200"/> | <img src="screenshots/history.png" width="200"/> |

> *Note: Giao diện được thiết kế theo phong cách Minimalist với tông màu chủ đạo Xanh Dương (#1877F2) và Trắng.*

---

## 🛠️ Tech Stack (Công nghệ sử dụng)

### 📱 Android Core
*   **Language:** Java.
*   **Layouts:** `LinearLayout`, `RelativeLayout`, `ScrollView`.
*   **Lists:** `ListView`, `GridView` (Custom `BaseAdapter` & `ArrayAdapter`).
*   **Networking:** `Retrofit 2` + `Gson` Converter.
*   **Concurrency:** `LiveData`, `ViewModel` (MVVM Architecture).

### 🎨 UI/UX
*   **Image Loading:** `Glide`.
*   **Custom Drawables:** Sử dụng XML Shape (`corners`, `gradient`, `stroke`) thay vì ảnh PNG để tối ưu hiệu năng.
*   **Dialogs:** Custom Alert Dialogs cho thông báo lỗi/thành công.

### ⚙️ Backend Integration
*   **API:** RESTful API.
*   **Auth:** JWT (JSON Web Token).
*   **Interceptor:** `AuthInterceptor` (Attach Token), `TokenExpirationInterceptor` (Handle 401/403 Errors).

---

## 🚀 Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/3TLap-Android.git
    ```
2.  **Open in Android Studio:** File -> Open -> Select project folder.
3.  **Configure API URL:**
    *   Go to `api/ApiClient.java`.
    *   Change `BASE_URL` to your local IP or deployed server URL.
    ```java
    public static final String BASE_URL = "http://192.168.1.x:8080/";
    ```
4.  **Build & Run:** Connect device/emulator and press Run (Shift + F10).

---

## 🤝 Contributing

Contributions are always welcome!
1.  Fork the project.
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the Branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

---

## 👨‍💻 Author

**Nguyen Trung Hieu (Your Name)**
*   **Role:** Android Developer (Student).
*   **Project:** Final Term Project.
*   **Contact:** [Your Email]

---

<p align="center"> Made with ❤️ and Java </p>

---

## 📂 Project Structure
