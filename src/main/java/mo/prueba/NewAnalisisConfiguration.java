/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba;

import java.io.File;
import mo.organization.Configuration;

/**
 *
 * @author Jorge
 */
public interface NewAnalisisConfiguration extends Configuration{
    void init(File folder);
    void count();
}
