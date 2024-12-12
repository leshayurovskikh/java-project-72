package hexlet.code.urls;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> urlChecks;

    public UrlPage(Url url) {
        this.url = url;
    }
}
