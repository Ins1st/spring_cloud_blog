package com.bite.blog.service;

import com.bite.blog.pojo.AddBlogInfoRequest;
import com.bite.blog.pojo.UpBlogRequest;
import com.bite.blog.pojo.BlogInfoResponse;

import java.util.List;

public interface BlogService {
    List<BlogInfoResponse> getList();

    BlogInfoResponse getBlogDeatil(Integer blogId);

    Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest);

    Boolean update(UpBlogRequest upBlogRequest);

    Boolean delete(Integer blogId);
}
