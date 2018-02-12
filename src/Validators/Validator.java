package Validators;

public interface Validator<T>
{
    void Validate(T s) throws Exception;
}
