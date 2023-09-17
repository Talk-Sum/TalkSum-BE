package talksum.talksum.service.SummaryService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GPTResponse {
    String id, object, model;
    String created;
    GPTResponseChoice[] choices;
    GPTResponseUsage usage;

    public GPTResponse(String id, String object, String model, String created, GPTResponseChoice[] choices, GPTResponseUsage usage) {
        this.id = id;
        this.object = object;
        this.model = model;
        this.created = created;
        this.choices = choices;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public String getModel() {
        return model;
    }

    public String getCreated() {
        return created;
    }

    public GPTResponseChoice[] getChoices() {
        return choices;
    }

    public GPTResponseUsage getUsage() {
        return usage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setChoices(GPTResponseChoice[] choices) {
        this.choices = choices;
    }

    public void setUsage(GPTResponseUsage usage) {
        this.usage = usage;
    }
}
