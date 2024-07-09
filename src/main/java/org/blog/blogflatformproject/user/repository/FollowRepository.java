package org.blog.blogflatformproject.user.repository;

import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    Follow findByFollowerAndFollowing(User follower , User following);

    //해당유저의 팔로워 숫자 불러오기
    @Query(value = "select count(*) from follow  where follower_id=?1",nativeQuery = true)
    int getFollowerCount(Long id);

    @Query(value = "select count(*) from follow  where following_id=?1",nativeQuery = true)
    int getFollowingCount(Long id);

    void deleteByFollowerAndFollowing(User follower,User following);
}
