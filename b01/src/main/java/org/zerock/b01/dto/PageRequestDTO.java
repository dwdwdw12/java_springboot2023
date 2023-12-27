package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default    //아무것도 전달하지 않은 채 사용
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;

    private String keyword;

    private String link;

    public String[] getTypes(){
        if(type==null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }
    public Pageable getPageable(String ... props){  //... : 가변인자(varargs), String 객체가 0개부터 여러 개까지 변수로 올 수 있다
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());    //2차 정렬조건
    }

    public String getLink(){
        if(link==null){
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type!=null && type.length()>0){
                builder.append("&type=" + type);
            }

            if(keyword!=null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));  //한글 깨짐 방지
                } catch (UnsupportedEncodingException e){

                }
            }
            link = builder.toString();
        }

        return link;

    }


}
