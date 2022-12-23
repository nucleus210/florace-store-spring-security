package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.view.ProductViewModel;
import lombok.Data;

@Data
public class QuestionDto {
    private String question;
    private ProductViewModel product;
}
