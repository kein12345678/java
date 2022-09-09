package mrs.app.room;

import java.time.LacalDate;
import java.until.List;

import mrs.domain.model.converter.ReservableRoom;
import mrs.domain.service.room.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.aanotation.RequestMethod;

@Controller
@RequestMapping("rooms")
public class RoomsController {
	@Autowired
	RoomService roomService;
		
	@RequestMapping(path = "{date}", method = RequestMethod.GET)
	String listRooms(Model model) {
		LocalDate today = LacalDate.now();
		List<ReservableRoom> rooms = roomService.findReservableRooms(today);
		model.addAllAttributes("date", today);
		model.addAllAttributes("rooms", rooms);
		return "room/listRooms";
	}
}
