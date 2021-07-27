public class Person
{
    private int _id;
    private String _name;
    private String _secondName;
    private String _patronymic;
    private String _address;
    private String _phone;
    private String _workStudPlace;
    private String _datingsNature;
    private String _businessQuality;
    private int _position;
    private String _fio;

    public void set_id(int _id)
    {
        this._id = _id;
    }
    public void set_name(String _name)
    {
        this._name = _name;
    }
    public void set_secondName(String _secondName)
    {
        this._secondName = _secondName;
    }
    public void set_patronymic(String _patronymic)
    {
        this._patronymic = _patronymic;
    }
    public void set_address(String _address)
    {
        this._address = _address;
    }
    public void set_phone(String _phone)
    {
        this._phone = _phone;
    }
    public void set_workStudPlace(String _workStudPlace)
    {
        this._workStudPlace = _workStudPlace;
    }
    public void set_datingsNature(String _datingsNature)
    {
        this._datingsNature = _datingsNature;
    }
    public void set_businessQuality(String _businessQuality)
    {
        this._businessQuality = _businessQuality;
    }
    public void set_position(int _position)
    {
        this._position = _position;
    }

    public int get_id()
    {
        return _id;
    }
    public String get_name()
    {
        return _name;
    }
    public String get_secondName()
    {
        return _secondName;
    }
    public String get_patronymic()
    {
        return _patronymic;
    }
    public String get_address()
    {
        return _address;
    }
    public String get_phone()
    {
        return _phone;
    }
    public String get_workStudPlace()
    {
        return _workStudPlace;
    }
    public String get_datingsNature()
    {
        return _datingsNature;
    }
    public String get_businessQuality()
    {
        return _businessQuality;
    }
    public String get_fio()
    {
        return toString();
    }

    public int get_position()
    {
        return _position;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        if(_secondName != null)
            sb.append(_secondName).append(" ");

        if(_name != null)
        {
            char[] charArr = _name.toCharArray();
            String nameChar = String.valueOf(charArr[0]);
            sb.append(nameChar.toUpperCase()).append(". ");
        }
        if(_patronymic != null)
        {
            char[] charArr = _patronymic.toCharArray();
            String patronymicChar = String.valueOf(charArr[0]);
            sb.append(patronymicChar.toUpperCase()).append(".");
        }
        return sb.toString();
    }
}
