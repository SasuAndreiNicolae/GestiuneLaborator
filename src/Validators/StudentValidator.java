package Validators;

import Domains.Student;
import Exceptions.StudentValidatorException;

public class StudentValidator implements Validator<Student>
{
    public StudentValidator(){}

    @Override
    public void Validate(Student s) throws StudentValidatorException
    {
        String msg="";
        if(s.getNume().compareTo("")==0)
            msg=msg+"Invalid name!\n";
        if(s.getNrMatricol()<0)
            msg+=msg+"Invalid id!\n";
        if(s.getGrupa()<0)
            msg+="Invalid group!\n";
        if(s.getEmail().isEmpty())
            msg+="Invalid e-mail adress!\n";
        if(s.getProf().isEmpty())
            msg+="Invalid professor name!\n";
        if(!msg.isEmpty()) {
            throw new StudentValidatorException(msg);
        }
    }
}
