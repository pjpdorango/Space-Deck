/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package spacedeck.exceptions;

/**
 *
 * @author pj
 */
public class FullFieldException extends Exception {

	/**
	 * Creates a new instance of <code>FullFieldException</code> without detail
	 * message.
	 */
	public FullFieldException() {
	}

	/**
	 * Constructs an instance of <code>FullFieldException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public FullFieldException(String msg) {
		super(msg);
	}
}
