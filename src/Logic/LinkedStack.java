package Logic;

class Node<T>
{
    private T value;
    private Node<T> previous, next;
    public Node()
    {
        previous = null;
    }

    public Node(T value)
    {
        this.value = value;
        previous = next = null;
    }

    public T getValue() {
        return value;
    }

    public Node<T> getPrevious() {
        return previous;
    }
    public Node<T> getNext()
    {
        return next;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }
    public void setNext(Node<T> next)
    {
        this.next = next;
    }

}


public class LinkedStack<T>
{
    private Node<T> top, first;
    private int size, length;

    public LinkedStack()
    {
        size = length = 0;
        top = first = null;
    }

    public void push(T value)
    {
        Node<T> tmp = new Node<T>(value);
        if(size == 0)
        {
            first = tmp;
        }
        if(size > 0)
        {
            tmp.setPrevious(top);
            top.setNext(tmp);
        }
        top = tmp;
        size++;
        clean();
    }

    public void pop()
    {
        if(size > 0)
        {
            top = top.getPrevious();
            size--;
        }
    }

    public boolean rePush()
    {
        if(size < length)
        {
            if(size == 0)
                top = first;
            else
                top = top.getNext();
            size++;
            return true;
        }
        return false;
    }

    public void clean()
    {
        if(top != null && top.getNext() != null)
        {
            Node<T> tmp = top.getNext();
            tmp.setPrevious(null);
            top.setNext(null);
        }
        else if (top == null && first != null)
        {
            Node<T> tmp = first.getNext();
            tmp.setPrevious(null);
            first.setNext(null);
        }
        length = size;
    }

    public T Top()
    {
        if(size > 0)
            return top.getValue();
        return null;
    }

    public T previous()
    {
        if(size > 1)
            return top.getPrevious().getValue();
        return null;
    }

    public int getSize()
    {
        return size;
    }

    public int getLength()
    {
        return length;
    }
}
