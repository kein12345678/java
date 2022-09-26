package mrs.domain.service.reservation;

import java.util.List;
import java.util.Objects;

import mrs.domain.model.*;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;//追加

@Service
@Transactional
public class ReservationService {
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ReservableRoomRepository reservableRoomRepository;
	
	public Reservation reserve(Reservation reservation) {
		ReservableRoomId reservableRoomId=reservation.getReservableRoom().getReservableRoomId();
		//悲観ロック
		ReservableRoom reservable = reservableRoomRepository.findOneForUpdateByReservableRoomId(reservableRoomId);
		//対象の部屋が予約可能かどうかチェックする
		// ReservableRoom reservable=reservableRoomRepository.findById(reservableRoomId).orElseGet(null);
		if (reservable==null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
		}
		//重複チェック
		boolean overlap=reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId)
				.stream()
				.anyMatch(x -> x.overlap(reservation));
		if(overlap) {
			throw new AlreadyReservedException("入力の時間帯はすでに予約済みです。");
		}
		//予約情報の登録
		reservationRepository.save(reservation);
		return reservation;
	}
	//その他処理は以下
	//予約可能部屋のIDを指定してReservationのリストを返すメソッド
	public List<Reservation> findReservations(ReservableRoomId reservableRoomId){
		return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId);
	}
	//予約キャンセルのメソッド
	public void cancel(Integer reservationId, User requestUser) {
		Reservation reservation=reservationRepository.findById(reservationId).orElseGet(null);
		if (RoleName.ADMIN != requestUser.getRoleName()&&
			!Objects.equals(reservation.getUser().getUserId(), requestUser.getUserId())){
				throw new AccessDeniedException("要求されたキャンセルは許可できません。");
			}
			reservationRepository.delete(reservation);
	}
	

}