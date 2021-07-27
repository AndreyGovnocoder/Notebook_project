public class Position
{
    private int _id;
    private String _position;

    public void set_id(int _id)
    {
        this._id = _id;
    }
    public void set_position(String _position)
    {
        this._position = _position;
    }

    public int get_id()
    {
        return _id;
    }
    public String get_position()
    {
        return _position;
    }

    @Override
    public String toString()
    {
        return _position;
    }
}
