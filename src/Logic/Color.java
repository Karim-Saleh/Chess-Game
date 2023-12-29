package Logic;

public enum Color {
    White, Black;
    public static Color getColor(int index)
    {
        switch(index)
        {
            case 0:
                return White;
            case 1:
                return Black;
            default:
                return null;
        }
    }
}