package Api.senders;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConsumeClass implements Serializable {

    private Long id;
    private String name;
}
