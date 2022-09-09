package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import mrs.domain.model.converter.ReservableRoom;
import mrs.domain.model.converter.ReservableRoomId;

import org.springframework.data.jpa.repository.JpaRepository;

public class ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId>{
	List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate reservedDate);
}
