package kr.co.there.place.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import kr.co.there.place.model.entity.PlaceVo;
import kr.co.there.review.model.entity.ReviewVo;

public interface PlaceDao {

	List<PlaceVo> selectAll() throws SQLException;
	PlaceVo selectOne(int place_idx) throws SQLException;
	int insertOne(PlaceVo bean) throws SQLException;
	int updateOne(PlaceVo bean) throws SQLException;
	int updateViewCnt(int place_idx) throws SQLException;
	int deleteOne(int place_idx) throws SQLException;
	List<PlaceVo> selectAllHome() throws SQLException;
	List<PlaceVo> orderBylikes() throws SQLException;
	
	List<ReviewVo> selectReviewAll() throws SQLException;
	List<ReviewVo> selectReviewbyPlace(int place_idx) throws SQLException;
	int insertReview(ReviewVo bean) throws SQLException;
	int countReview(int place_idx) throws SQLException;
	double avgScore(int place_idx) throws SQLException;
	
	int countLike(int place_idx) throws SQLException;

}
