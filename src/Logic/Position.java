package Logic;

public class Position {
    public int column;
    public int row;

    public Position(){ }

    public Position(int column, int row){
        this.column = column;
        this.row = row;
    }

    public Position(Position position){
        column = position.column;
        row = position.row;
    }

    public Position subtract(Position position)
    {
        return new Position(position.column - this.column, position.row - this.row);
    }

    public static boolean equals(Position first, Position second)
    {
        return (first != null && second != null && first.column == second.column && first.row == second.row);
    }

    public static void copyArray(Position []destination, Position[] source)
    {
        int i = 0;
        for(Position pos : source)
        {
            destination[i] = new Position(pos);
            i++;
        }
    }

    public static boolean exist(Position position, Position[] arr)
    {
        for (Position pos : arr)
        {
            if (equals(position, pos))
            {
                return true;
            }
        }
        return false;
    }
}
