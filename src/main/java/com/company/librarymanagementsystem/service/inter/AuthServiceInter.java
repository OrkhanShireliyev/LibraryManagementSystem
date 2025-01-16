package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.RegisterDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthServiceInter {
  void register(RegisterDto registerDto);
}
