using Microsoft.AspNetCore.Mvc;
using olxApp.Models;
using System;
using System.Data;
using Microsoft.Data.SqlClient;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace olxApp.Controllers
{
    public class UserController : Controller
    {
        private readonly string _connectionString = "Data Source=(localdb)\\MSSQLLocalDB;Initial Catalog=OlxApp;Integrated Security=True;Connect Timeout=30;Encrypt=True;Trust Server Certificate=False;Application Intent=ReadWrite;Multi Subnet Failover=False";

        [HttpPost]
        public async Task<IActionResult> Register([FromBody] RegisterRequest request)
        {
            Console.WriteLine(request.Username);
            Console.WriteLine(request.Password);    
            Console.WriteLine(request.Email);

            if (request == null || string.IsNullOrEmpty(request.Email) || string.IsNullOrEmpty(request.Password))
            {
                return BadRequest(false);   
            }

            try
            {
                using (SqlConnection connection = new SqlConnection(_connectionString))
                {
                    await connection.OpenAsync();

                    // Check if the user already exists
                    string checkUserQuery = "SELECT COUNT(1) FROM Users WHERE Email = @Email";
                    using (SqlCommand checkCommand = new SqlCommand(checkUserQuery, connection))
                    {
                        checkCommand.Parameters.AddWithValue("@Email", request.Email);
                        int userExists = (int)await checkCommand.ExecuteScalarAsync();

                        if (userExists > 0)
                        {
                            return Conflict(false);
                        }
                    }

                    // Insert the new user
                    string insertQuery = @"
                        INSERT INTO Users (Name, Email, Password)
                        VALUES (@Name, @Email, @Password)";

                    using (SqlCommand insertCommand = new SqlCommand(insertQuery, connection))
                    {
                        insertCommand.Parameters.AddWithValue("@Name", request.Username ?? (object)DBNull.Value);
                       
                        insertCommand.Parameters.AddWithValue("@Email", request.Email);
                        insertCommand.Parameters.AddWithValue("@Password", request.Password); // Hash passwords in real apps

                        await insertCommand.ExecuteNonQueryAsync();
                    }
                }

                return  Ok(true);
            }
            catch (Exception)
            {
                return StatusCode(500, false);
            }
        }

        [HttpPost]


        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            Console.WriteLine(request.email);   
            if (request == null || string.IsNullOrEmpty(request.email) || string.IsNullOrEmpty(request.password))
            {
                return BadRequest(false);
            }

            try
            {
                using (SqlConnection connection = new SqlConnection(_connectionString))
                {
                    await connection.OpenAsync();

                    // Validate user credentials
                    string query = "SELECT Id, Password FROM Users WHERE Email = @Email";
                    using (SqlCommand command = new SqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@Email", request.email);

                        using (SqlDataReader reader = await command.ExecuteReaderAsync())
                        {
                            if (await reader.ReadAsync())
                            {
                                var id = reader.GetInt32(reader.GetOrdinal("Id"));
                                var storedPassword = reader.GetString(reader.GetOrdinal("Password"));

                                if (storedPassword != request.password) // Compare hashed passwords in production
                                {
                                    return Unauthorized(false); // Invalid credentials
                                }

                                // Credentials are valid; you can proceed with further logic
                                return Ok(new { Success = true, UserId = id });
                            }
                            else
                            {
                                return Unauthorized(false); // Email not found
                            }
                        }
                    }
                }


                return Ok(true); // Login successful
            }
            catch (Exception)
            {
                return StatusCode(500, false); // Internal server error
            }
        }


        [HttpPost]
        public async Task<IActionResult> PostAd([FromForm] string ad, IFormFile image)
        {
            // Deserialize the ad JSON into an Ad object
            var adDetails = JsonConvert.DeserializeObject<Ad>(ad);

            // Log the ad details to confirm it's deserialized
            Console.WriteLine($"in post add{adDetails.Title} - {adDetails.userId}");

            string imageUrl = null;

            // Handle the image upload
            if (image != null && image.Length > 0)
            {
                // Generate a file path for the image
                var fileName = Guid.NewGuid().ToString() + Path.GetExtension(image.FileName);
                var filePath = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot/images", fileName);

                // Save the image to the server
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    await image.CopyToAsync(stream);
                }

                // Set the image URL (relative path)
                imageUrl = "/images/" + fileName;
            }

            // Save the ad details in the database using ADO.NET
           
            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                await connection.OpenAsync();

                var query = @"
            INSERT INTO Ads (Title, Description, Price, Condition, Category, ShowPhoneNumber, ImageUrl, userId)
            VALUES (@Title, @Description, @Price, @Condition, @Category, @ShowPhoneNumber, @ImageUrl,@userId);
            SELECT SCOPE_IDENTITY();"; // Get the Id of the newly inserted ad

                using (var command = new SqlCommand(query, connection))
                {
                    // Add parameters to prevent SQL injection
                    command.Parameters.AddWithValue("@Title", adDetails.Title);
                    command.Parameters.AddWithValue("@Description", adDetails.Description);
                    command.Parameters.AddWithValue("@Price", adDetails.Price);
                    command.Parameters.AddWithValue("@Condition", adDetails.Condition);
                    command.Parameters.AddWithValue("@Category", adDetails.Category);
                    command.Parameters.AddWithValue("@ShowPhoneNumber", adDetails.ShowPhoneNumber);
                    command.Parameters.AddWithValue("@ImageUrl", imageUrl);
                    command.Parameters.AddWithValue("@userId", adDetails.userId);

                    // Execute the command and get the Id of the newly inserted ad
                    var adId = await command.ExecuteScalarAsync();
                    Console.WriteLine($"Ad saved with Id: {adId}");
                }
            }

            // Return success
            return Ok(true);
        }



        [HttpGet]
        public async Task<IActionResult> GetAllAds(int? id)
        {
            Console.WriteLine("in get all ads");
            var adsList = new List<Ad>();

            using (SqlConnection connection = new SqlConnection(_connectionString))
            {
                await connection.OpenAsync();
                var query=string.Empty;
                if (id == null)
                {
                    query = "SELECT Id, Title, Description, Price, Condition, Category, ShowPhoneNumber, ImageUrl,userId FROM Ads";
                }
                else
                {
                    query = $"SELECT Id, Title, Description, Price, Condition, Category, ShowPhoneNumber, ImageUrl,userId FROM Ads where userId={id}";
                }


                 using (var command = new SqlCommand(query, connection))
                {
                    using (var reader = await command.ExecuteReaderAsync())
                    {
                        while (await reader.ReadAsync())
                        {
                            var ad = new Ad
                            {
                                Id = reader.GetInt32(0),
                                Title = reader.GetString(1),
                                Description = reader.GetString(2),
                                Price = reader.GetDecimal(3),
                                Condition = reader.GetString(4),
                                Category = reader.GetString(5),
                                ShowPhoneNumber = reader.GetBoolean(6),
                                ImageUrl = reader.IsDBNull(7) ? null : reader.GetString(7),
                                userId=reader.GetInt32(8),
                            };

                            adsList.Add(ad);
                        }
                    }
                }
            }

            // If no ads are found, return a NotFound response
            if (adsList.Count == 0)
            {
                return NotFound("No ads found.");
            }

            return Ok(adsList);
        }




        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateAd(int id, [FromBody] Ad request)
        {
            if (request == null)
            {
                return BadRequest("Invalid request");
            }

            try
            {
                using (SqlConnection connection = new SqlConnection(_connectionString))
                {
                    await connection.OpenAsync();

                    // Check if the ad exists
                    string checkAdQuery = "SELECT COUNT(1) FROM Ads WHERE Id = @Id";
                    using (SqlCommand checkCommand = new SqlCommand(checkAdQuery, connection))
                    {
                        checkCommand.Parameters.AddWithValue("@Id", id);
                        int adExists = (int)await checkCommand.ExecuteScalarAsync();

                        if (adExists == 0)
                        {
                            return NotFound("Ad not found");
                        }
                    }

                    // Update the ad
                    string updateQuery = @"
                UPDATE Ads
                SET Title = @Title,
                    Description = @Description,
                    Price = @Price,
                    Condition = @Condition,
                    Category = @Category,
                    ShowPhoneNumber = @ShowPhoneNumber,
                    ImageUrl = @ImageUrl
                WHERE Id = @Id";

                    using (SqlCommand updateCommand = new SqlCommand(updateQuery, connection))
                    {
                        updateCommand.Parameters.AddWithValue("@Id", id);
                        updateCommand.Parameters.AddWithValue("@Title", request.Title);
                        updateCommand.Parameters.AddWithValue("@Description", request.Description);
                        updateCommand.Parameters.AddWithValue("@Price", request.Price);
                        updateCommand.Parameters.AddWithValue("@Condition", request.Condition);
                        updateCommand.Parameters.AddWithValue("@Category", request.Category);
                        updateCommand.Parameters.AddWithValue("@ShowPhoneNumber", request.ShowPhoneNumber);
                        updateCommand.Parameters.AddWithValue("@ImageUrl", request.ImageUrl ?? (object)DBNull.Value);

                        int rowsAffected = await updateCommand.ExecuteNonQueryAsync();

                        if (rowsAffected == 0)
                        {
                            return NotFound("Ad not found");
                        }
                    }
                }

                return Ok(true); // Return success response
            }
            catch (Exception)
            {
                return StatusCode(500, "Internal server error");
            }
        }


        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAd(int id)
        {

            Console.WriteLine($"dele ad with id {id}");
            try
            {
                using (SqlConnection connection = new SqlConnection(_connectionString))
                {
                    await connection.OpenAsync();

                    // Check if the ad exists
                    string checkAdQuery = "SELECT COUNT(1) FROM Ads WHERE Id = @Id";
                    using (SqlCommand checkCommand = new SqlCommand(checkAdQuery, connection))
                    {
                        checkCommand.Parameters.AddWithValue("@Id", id);
                        int adExists = (int)await checkCommand.ExecuteScalarAsync();

                        if (adExists == 0)
                        {
                            return NotFound("Ad not found");
                        }
                    }

                    // Delete the ad
                    string deleteQuery = "DELETE FROM Ads WHERE Id = @Id";
                    using (SqlCommand deleteCommand = new SqlCommand(deleteQuery, connection))
                    {
                        deleteCommand.Parameters.AddWithValue("@Id", id);
                        await deleteCommand.ExecuteNonQueryAsync();
                    }
                }

                return Ok(true); // Return success response
            }
            catch (Exception)
            {
                return StatusCode(500, "Internal server error");
            }
        }






    }
}
public class Ad
{
    public int Id { get; set; }
    public string Title { get; set; }
    public string Description { get; set; }
    public decimal Price { get; set; }
    public string Condition { get; set; }
    public string Category { get; set; }
    public bool ShowPhoneNumber { get; set; }
    public string ImageUrl { get; set; } // This will hold the image URL
    public int userId { get; set; }
}

