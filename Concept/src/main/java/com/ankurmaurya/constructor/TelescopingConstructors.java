package com.ankurmaurya.constructor;

public class TelescopingConstructors {

	/*
	 * 
	 * Unfortunately, the telescoping constructors will not prevent you from having to pass null values in some cases. 
	 * For instance, if you had to create a Book with ISBN, title, and author, what would you do? There is no such constructor!
	 * 
	 * 
	 * This would not work. 
	 *   public Book(String isbn, String title, String author) {
		    this.isbn = isbn;
		    this.title = title;
		    this.author = author;
		 }
		
		 public Book(String isbn, String title, String description) {
		    this.isbn = isbn;
		    this.title = title;
		    this.description = description;
		 }
		 
	 *  Two constructors of the same signature cannot coexist in the same class, because the compiler would not know which one to choose.
	 * 
	 */
	
	public static void main(String[] args) {
		
		Book book1 = new Book("IFG3456TYUI", "Effective Java");
		System.out.println("Book 1 - " + book1);
		
		Book book2 = new Book("IFG3456TYUI", "Effective Java", "Technical");
		System.out.println("Book 2 - " + book2);
		
	}
	
	
	

}
