package com.example.demo.controller;

import com.example.demo.pojo.Book;
import com.example.demo.service.BookService;
import com.example.demo.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    BookService bookService;

    @CrossOrigin
    @GetMapping("/api/books")
    public List<Book> list() throws Exception{
        return bookService.list();
    }

    @CrossOrigin
    @PostMapping("/api/books")
    public Book addOrUpdate(@RequestBody Book book) throws Exception{
        bookService.addOrUpdate(book);
        return book;
    }

    @CrossOrigin
    @PostMapping("/api/delete")
    public void delete(@RequestBody Book book) throws Exception{
        bookService.deleteById(book.getId());
    }

    @CrossOrigin
    @GetMapping("/api/categories/{cid}/books")
    public List<Book> listByCategory(@PathVariable("cid") int cid) throws Exception{
        if (cid!=0){
            return bookService.listByCategory(cid);
        }else{
            return list();
        }
    }

    @CrossOrigin
    @GetMapping("/api/search")
    public List<Book> searchResult(@RequestParam("keywords") String keywords){
        if ("".equals(keywords)){
            return bookService.list();
        }else{
            return bookService.Search(keywords);
        }
    }

    @CrossOrigin
    @PostMapping("api/covers")
    public String coversUpload(MultipartFile file) throws Exception{
        String folder = "D:/workspace/img";
        File imageFolder =new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6)+file.getOriginalFilename()
                .substring(file.getOriginalFilename().length()-4));
        if(!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try{
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/" +f.getName();
            return imgURL;
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }

    }
}
