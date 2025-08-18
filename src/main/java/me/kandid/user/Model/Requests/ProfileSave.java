package me.kandid.user.Model.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.File;

@Data
@Schema(
        title = "Customer Object Profile Upload",
        description = "Details about the customer"
)
public class ProfileSave {
    @Id
    @Schema(
            title = "Phone Number",
            example = "9161086557"
    )
    private long phone;
    @Schema(
            title = "Name",
            example = "Samriddh Verma"
    )
    private String name;
    @Schema(
            title = "Email",
            example = "samriddh@kandid.me"
    )
    private String email;
    @Schema(
            title = "Profile Picture",
            description = "Unique identifier for the profile picture stored in aws bucket, if present",
            example = "https://aws.com/bucket/sdadmkasdma"
    )
    private File profilePicture;
    @Schema(
            title = "Gender",
            example = "MALE"
    )
    private String gender;
}
