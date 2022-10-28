package com.ankurmaurya.constructor;

public class Book {
    private final String isbn;
    private final String title;
    private final String genre;
    private final String author;
    private final String published;
    private final String description;

    public Book(String isbn, String title) {
        this(isbn, title, null);
    }

    public Book(String isbn, String title, String genre) {
        this(isbn, title, genre, null);
    }

    public Book(String isbn, String title, String genre, String author) {
        this(isbn, title, genre, author, null);
    }

    public Book(String isbn, String title, String genre, String author, String published) {
        this(isbn, title, genre, author, published, null);
    }

    public Book(String isbn, String title, String genre, String author, String published, String description) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.published = published;
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getDescription() {
        return description;
    }

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", genre=" + genre + ", author=" + author + ", published="
				+ published + ", description=" + description + "]";
	}

    
    
    
}
