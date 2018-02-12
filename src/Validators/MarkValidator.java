package Validators;

import Domains.Mark;
import Exceptions.MarkException;

public class MarkValidator implements Validator<Mark>
{
    public MarkValidator()
    {
    }

    @Override
    public void Validate(Mark s) throws MarkException
    {
        String err="";
        if(s.getValoare()<0||s.getValoare()>10)
            err+="Valoarea notei trebuie sa fie intre [1,10]!\n";
        if(!err.isEmpty())
            throw new MarkException(err);
    }
}
