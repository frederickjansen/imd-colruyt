package be.alfredo.colruyt;

/**
 * Data Transfer Object of a product with a name, price and price difference.
 */
public class ComparisonResults
{
    private String product;
    private Number difference;
    private Number price;

    public String getDifference()
    {
        return difference.toString();
    }

    public void setDifference(Number difference)
    {
        this.difference = difference;
    }

    public String getPrice()
    {
        return price.toString();
    }

    public void setPrice(Number price)
    {
        this.price = price;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getProduct()
    {
        return product;
    }
}
