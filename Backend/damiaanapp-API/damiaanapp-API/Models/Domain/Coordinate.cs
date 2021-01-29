namespace damiaanapp_API.Models.Domain
{
    public class Coordinate
    {
        #region Properties
        public int Id { get; set; }
        public double Longitude { get; set; }
        public double Latitude { get; set; }
        #endregion

        #region Constructors
        public Coordinate()
        {

        }

        public Coordinate(double lon, double lat)
        {
            Longitude = lon;
            Latitude = lat;
        }
        #endregion

    }
}
