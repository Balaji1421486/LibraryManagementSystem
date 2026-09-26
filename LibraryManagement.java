package librarymanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class LibraryManagement {

    static final String URL =
            "jdbc:mysql://localhost:3306/library_management";
    static final String USER = "root";
    static final String PASSWORD = "root@123";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

            while (true) {

                System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Book");
                System.out.println("2. View Books");
                System.out.println("3. Search Book");
                System.out.println("4. Add Member");
                System.out.println("5. View Members");
                System.out.println("6. Issue Book");
                System.out.println("7. Return Book");
                System.out.println("8. View Issue Details");
                System.out.println("9. Update Book");
                System.out.println("10. Delete Book");
                System.out.println("11. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        addBook(con);
                        break;

                    case 2:
                        viewBooks(con);
                        break;

                    case 3:
                        searchBook(con);
                        break;

                    case 4:
                        addMember(con);
                        break;

                    case 5:
                        viewMembers(con);
                        break;

                    case 6:
                        issueBook(con);
                        break;

                    case 7:
                        returnBook(con);
                        break;

                    case 8:
                        viewIssueDetails(con);
                        break;

                    case 9:
                        updateBook(con);
                        break;

                    case 10:
                        deleteBook(con);
                        break;

                    case 11:
                        System.out.println("Thank you!");
                        con.close();
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 1. Add Book
    static void addBook(Connection con) {

        try {
            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Author: ");
            String author = sc.nextLine();

            System.out.print("Enter Category: ");
            String category = sc.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();
            sc.nextLine();

            String sql =
                    "INSERT INTO books " +
                    "(title, author, category, quantity, available_quantity) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4, quantity);
            ps.setInt(5, quantity);

            ps.executeUpdate();

            System.out.println("Book Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 2. View Books
    static void viewBooks(Connection con) {

        try {
            String sql = "SELECT * FROM books";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- BOOK LIST ---");

            while (rs.next()) {

                System.out.println(
                        rs.getInt("book_id") + " | "
                        + rs.getString("title") + " | "
                        + rs.getString("author") + " | "
                        + rs.getString("category") + " | "
                        + "Total: " + rs.getInt("quantity") + " | "
                        + "Available: "
                        + rs.getInt("available_quantity"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 3. Search Book
    static void searchBook(Connection con) {

        try {
            System.out.print("Enter Book Title to Search: ");
            String title = sc.nextLine();

            String sql =
                    "SELECT * FROM books WHERE title LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + title + "%");

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- SEARCH RESULT ---");

            while (rs.next()) {

                found = true;

                System.out.println(
                        rs.getInt("book_id") + " | "
                        + rs.getString("title") + " | "
                        + rs.getString("author") + " | "
                        + rs.getString("category") + " | "
                        + "Available: "
                        + rs.getInt("available_quantity"));
            }

            if (!found) {
                System.out.println("Book Not Found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4. Add Member
    static void addMember(Connection con) {

        try {
            System.out.print("Enter Member Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            String sql =
                    "INSERT INTO members " +
                    "(member_name, email, phone) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();

            System.out.println("Member Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5. View Members
    static void viewMembers(Connection con) {

        try {
            String sql = "SELECT * FROM members";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- MEMBER LIST ---");

            while (rs.next()) {

                System.out.println(
                        rs.getInt("member_id") + " | "
                        + rs.getString("member_name") + " | "
                        + rs.getString("email") + " | "
                        + rs.getString("phone"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 6. Issue Book
    static void issueBook(Connection con) {

        try {
            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter Member ID: ");
            int memberId = sc.nextInt();

            String checkBook =
                    "SELECT available_quantity FROM books WHERE book_id=?";

            PreparedStatement bookPs =
                    con.prepareStatement(checkBook);

            bookPs.setInt(1, bookId);

            ResultSet bookRs = bookPs.executeQuery();

            if (!bookRs.next()) {
                System.out.println("Book Not Found!");
                return;
            }

            int available = bookRs.getInt("available_quantity");

            if (available <= 0) {
                System.out.println("Book is not available!");
                return;
            }

            con.setAutoCommit(false);

            String issueSql =
                    "INSERT INTO book_issues " +
                    "(book_id, member_id, issue_date, status) " +
                    "VALUES (?, ?, ?, 'Issued')";

            PreparedStatement issuePs =
                    con.prepareStatement(issueSql);

            issuePs.setInt(1, bookId);
            issuePs.setInt(2, memberId);
            issuePs.setDate(3, Date.valueOf(LocalDate.now()));

            issuePs.executeUpdate();

            String updateBook =
                    "UPDATE books SET available_quantity = " +
                    "available_quantity - 1 WHERE book_id=?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateBook);

            updatePs.setInt(1, bookId);
            updatePs.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            System.out.println("Book Issued Successfully!");

        } catch (Exception e) {

            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 7. Return Book + Fine Calculation
    static void returnBook(Connection con) {

        try {
            System.out.print("Enter Issue ID: ");
            int issueId = sc.nextInt();

            String sql =
                    "SELECT book_id, issue_date FROM book_issues " +
                    "WHERE issue_id=? AND status='Issued'";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, issueId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Active issue record not found!");
                return;
            }

            int bookId = rs.getInt("book_id");

            LocalDate issueDate =
                    rs.getDate("issue_date").toLocalDate();

            LocalDate returnDate = LocalDate.now();

            long days =
                    ChronoUnit.DAYS.between(issueDate, returnDate);

            double fine = 0;

            // First 7 days are free.
            // After 7 days, fine = Rs.10 per day.
            if (days > 7) {
                fine = (days - 7) * 10;
            }

            con.setAutoCommit(false);

            String returnSql =
                    "UPDATE book_issues SET return_date=?, " +
                    "fine=?, status='Returned' WHERE issue_id=?";

            PreparedStatement returnPs =
                    con.prepareStatement(returnSql);

            returnPs.setDate(1, Date.valueOf(returnDate));
            returnPs.setDouble(2, fine);
            returnPs.setInt(3, issueId);

            returnPs.executeUpdate();

            String updateBook =
                    "UPDATE books SET available_quantity = " +
                    "available_quantity + 1 WHERE book_id=?";

            PreparedStatement bookPs =
                    con.prepareStatement(updateBook);

            bookPs.setInt(1, bookId);
            bookPs.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            System.out.println("Book Returned Successfully!");
            System.out.println("Days Kept: " + days);
            System.out.println("Fine: Rs." + fine);

        } catch (Exception e) {

            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("Error: " + e.getMessage());
        }
    }

    // 8. View Issue Details - JOIN
    static void viewIssueDetails(Connection con) {

        try {

            String sql =
                    "SELECT bi.issue_id, b.title, m.member_name, " +
                    "bi.issue_date, bi.return_date, bi.fine, bi.status " +
                    "FROM book_issues bi " +
                    "JOIN books b ON bi.book_id = b.book_id " +
                    "JOIN members m ON bi.member_id = m.member_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- ISSUE DETAILS ---");

            while (rs.next()) {

                System.out.println(
                        "Issue ID: " + rs.getInt("issue_id")
                        + " | Book: " + rs.getString("title")
                        + " | Member: " + rs.getString("member_name")
                        + " | Issue Date: " + rs.getDate("issue_date")
                        + " | Return Date: " + rs.getDate("return_date")
                        + " | Fine: Rs." + rs.getDouble("fine")
                        + " | Status: " + rs.getString("status"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 9. Update Book
    static void updateBook(Connection con) {

        try {

            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Title: ");
            String title = sc.nextLine();

            System.out.print("Enter New Author: ");
            String author = sc.nextLine();

            System.out.print("Enter New Category: ");
            String category = sc.nextLine();

            String sql =
                    "UPDATE books SET title=?, author=?, " +
                    "category=? WHERE book_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Updated Successfully!");
            } else {
                System.out.println("Book Not Found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 10. Delete Book
    static void deleteBook(Connection con) {

        try {

            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();

            String checkSql =
                    "SELECT COUNT(*) FROM book_issues WHERE book_id=?";

            PreparedStatement checkPs =
                    con.prepareStatement(checkSql);

            checkPs.setInt(1, id);

            ResultSet rs = checkPs.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println(
                        "Cannot delete this book because " +
                        "issue records exist.");
                return;
            }

            String sql = "DELETE FROM books WHERE book_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Deleted Successfully!");
            } else {
                System.out.println("Book Not Found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}