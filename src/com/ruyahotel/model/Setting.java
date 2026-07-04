package com.ruyahotel.model;

import java.time.LocalDateTime;

public class Setting {
    private int settingId;
    private String settingKey;
    private String settingValue;
    private String category;
    private LocalDateTime updatedAt;

    public Setting() {}

    public int getSettingId() { return settingId; }
    public void setSettingId(int settingId) { this.settingId = settingId; }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
