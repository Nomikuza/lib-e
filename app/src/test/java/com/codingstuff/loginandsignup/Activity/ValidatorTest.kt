package com.codingstuff.loginandsignup.Activity

//import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.google.common.truth.Truth.assertThat

@RunWith(JUnit4::class)
class ValidatorTest{
    @Test
    fun whenInputIsValid(){
        val email = "libe77299@gmail.com"
        val password = "1234"
        val result = Validator.validateInput(email, password)
        assertThat(result).isEqualTo(true)
    }//Jika hasil true maka valid, assert digunakan untuk memasukkan object, isEqualTo untuk mengecek hasil output yg dihasilkan


    @Test
    fun whenInputIsInvalid(){
        val email = ""
        val password = ""
        val result = Validator.validateInput(email, password)
        assertThat(result).isEqualTo(false)
    }//jika false maka invalid, assert digunakan untuk memasukkan object, isEqualTo untuk mengecek hasil output yg dihasilkan
}