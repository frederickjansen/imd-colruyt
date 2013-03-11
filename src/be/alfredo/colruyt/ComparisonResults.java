package be.alfredo.colruyt;

/**
 *
 */
public class ComparisonResults
{
    private String productName;
    private Number difference;
    private Number price;

    public Number getDifference()
    {
        return difference;
    }

    public void setDifference(Number difference)
    {
        this.difference = difference;
    }

    public Number getPrice()
    {
        return price;
    }

    public void setPrice(Number price)
    {
        this.price = price;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return productName;
    }
}
