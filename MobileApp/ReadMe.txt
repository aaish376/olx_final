This project is an OLX clone, a marketplace app that allows users to buy and sell items locally. The app has been developed using Android Studio with Kotlin and XML for UI design. Key features include a home screen, My Ads screen, Account section, Login/Logout functionality, and additional screens for viewing product details, adding new products for sale, and viewing details of the user's own ads. Items are displayed in a RecyclerView with custom layouts and adapters.

Features Implemented
1. Home Screen
Shows a list of available items for sale.
Each item is displayed using a custom layout with:
An image of the product.
The product title and price.
The location of the seller.
The time since the item was listed.
Items are displayed in a scrollable list using a RecyclerView, with an adapter to bind data to the views.
2. Product Detail Screen
Displays the full details of a selected product from the home screen.
Includes:
Larger image of the product.
Detailed description.
Contact details of the seller (e.g., phone number).
Additional product information such as price, location, and listing date.
Users can contact the seller directly from this screen.
3. Sell Screen
Allows users to post new ads for items they want to sell.
Includes form fields for:
Product title, price, description, and location.
An option to upload an image of the product.
Validates input fields to ensure required information is provided before posting.
Once submitted, the ad is added to the "My Ads" section for the user and displayed on the Home screen for other users.
4. My Ads Screen
Displays all items listed for sale by the logged-in user.
Similar to the Home Screen, it uses a RecyclerView to display the user's listed products.
Each ad displays an image, title, price, and location.
5. My Ads Detail Screen
Shows detailed information for a selected item from the My Ads screen.
Users can view the product description, price, and other details as listed.
Provides options for the user to edit or delete their ad.
Editing allows users to update the product information or upload a new image.
6. Account Section
Allows users to view and manage their profile details.
Includes options like viewing the public profile, editing profile details, and accessing the user's ads.
Features clickable text views and buttons that navigate to other sections, such as "Edit Profile" and "Public Profile."
7. Login and Logout
Simple login and logout flow implemented using Android's Intent.
The user can log in using the login screen and log out via the account section.
After logging in, the user is redirected to the home screen.
Screens
Home Screen

Displays a list of items for sale using a RecyclerView.
Items are displayed with an image, price, title, location, and time since listing.
Product Detail Screen

Shows full details of a selected item from the Home screen, allowing users to contact the seller.
Sell Screen

Provides an interface for users to post new ads with fields for title, price, description, location, and an option to upload a product image.
My Ads Screen

Shows all items posted by the user, displayed with a RecyclerView for easy scrolling.
My Ads Detail Screen

Allows the user to view, edit, or delete their posted ads.
Account Section

Displays user account information and navigation options to profile-related settings.
Includes buttons to edit the user's profile, view the public profile, and log out.
Login/Logout

Basic login functionality where users can input their credentials to access their account.
Logout functionality redirects the user to the login page.
Key Components
RecyclerView

Used for displaying lists of items in both the Home screen and My Ads screen.
Custom adapters are created to bind data (such as item image, title, and price) to the layout.
ConstraintLayout

Used across various screens to arrange UI elements in a flexible and responsive manner.
Enables a clean and organized UI with proper alignment and spacing.
Intent-based Navigation

Navigation between different screens (Home, My Ads, Account, etc.) is handled using Android's Intent mechanism.