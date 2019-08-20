package com.lb.sys.tools;

/**
 * 
 * @author Administrator
 *
 */
public class PublicKeyUtil {
	 private String modulus;
	 private String exponent;
	 public String getModulus() {
	  return modulus;
	 }
	 public void setModulus(String modulus) {
	  this.modulus = modulus;
	 }
	 public String getExponent() {
	  return exponent;
	 }
	 public void setExponent(String exponent) {
	  this.exponent = exponent;
	 }
	 public String toString() {
	  return "PublicKeyMap [modulus=" + modulus + ", exponent=" + exponent
	    + "]";
	 }
}
