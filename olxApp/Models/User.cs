namespace olxApp.Models
{
    public class User
    {
        public int Id { get; set; } // Primary Key
        public string Name { get; set; }
        public string Bio { get; set; }
        public string Gender { get; set; } // Optional: Use Enum for gender
        public DateTime DateOfBirth { get; set; } // Date of Birth
        public string PhoneNumber { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
    }
}
