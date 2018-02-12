package Validators;

import Domains.Homework;
import Exceptions.HomeworkValidatorException;

public class HomeworkValidator implements Validator<Homework>
{

    public HomeworkValidator()
    {
    }

    @Override
    public void Validate(Homework s) throws HomeworkValidatorException
    {
        String msg="";
        if(s.getNrTema()<0)
            msg+="Invalid homework number!\n";
        if(s.getEnunt().isEmpty())
            msg+="Invalid problem requirment!\n";
        if((s.getTermenLimita()<2 )|| (s.getTermenLimita()>14))
            msg+="Invalid deadline!";

        if(!msg.isEmpty())
            throw  new HomeworkValidatorException(msg);
    }
}
