package com.dev.moyering.socialing.entity;
import com.dev.moyering.common.entity.User;
import com.dev.moyering.socialing.dto.FollowDto;
import lombok.*;
import javax.persistence.*;
import com.dev.moyering.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"followerId", "followingId"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followingId", nullable = false)
    private User following;

    public FollowDto toDto() {
        return FollowDto.builder()
                .id(id)
                .followerId(follower.getUserId())
                .followingId(following.getUserId())
                .build();
    }
}
