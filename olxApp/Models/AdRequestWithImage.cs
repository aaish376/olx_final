namespace olxApp.Models
{
    

    public class AdRequestWithImage
    {
        public string Title { get; set; }
        public string? Description { get; set; }
        public decimal Price { get; set; }
        public string? Category { get; set; }
        public int UserId { get; set; }
        public IFormFile? Image { get; set; }
    }

}
