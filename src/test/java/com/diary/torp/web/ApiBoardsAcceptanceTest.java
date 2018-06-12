package com.diary.torp.web;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.domain.*;
import com.diary.torp.service.ToDoService;
import com.diary.torp.support.AcceptanceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiBoardsAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiBoardsAcceptanceTest.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Resource(name = "toDoDeckRepository")
    private ToDoDeckRepository toDoDeckRepository;

    @Resource(name = "toDoCardRepository")
    private ToDoCardRepository toDoCardRepository;

    @Test
    public void createBoard() {
        //when loginUser try to create Board.
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard1",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        log.debug("body : {}", response.getBody());

        //when 'guest' try to create Deck.
        ResponseEntity<String> response2 = template().postForEntity("/api/boards/add", "testBoard2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When loginUser try to create Board.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> response3 = basicAuthTemplate(javajigi).postForEntity("/api/boards/add", "testBoard3",  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void createDeck() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard4",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard4");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());

        //when 'board owner' try to create Deck.
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //when 'guest' try to create Deck.
        ResponseEntity<String> wrongResponse = template().postForEntity(url, "testDeck2",  String.class);
        assertThat(wrongResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When another try to create Deck in not owned board.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> wrongResponse2 = basicAuthTemplate(javajigi).postForEntity(url, "testDeck3",  String.class);
        assertThat(wrongResponse2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        log.debug("body : {}", response.getBody());
    }

    @Test
    public void createCard() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard5",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard5");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck4",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //when 'Deck owner' try to create Card.
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck4");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //when 'guest' try to create Deck.
        ResponseEntity<String> wrongResponse = template().postForEntity(url2, "testCard2", String.class);
        assertThat(wrongResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When another try to create Card in not owned Deck.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> wrongResponse2 = basicAuthTemplate(javajigi).postForEntity(url2, "testCard3",  String.class);
        assertThat(wrongResponse2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        log.debug("body : {}", response.getBody());
    }

    @Test
    public void getCardInfo() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard6",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard6");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck5",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck5");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard4", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //when 'card owner' try to access card information.
        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard4");
        String resultUrl = String.format("/api/boards/%d/%d/%d/cardInfo", targetBoard.getId(), targetDeck.getId(), targetCard.getId());
        ResponseEntity<String> response4 = basicAuthTemplate().postForEntity(resultUrl, null, String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.OK));

        //when 'guest' try to access card information.
        ResponseEntity<String> wrongResponse = template().postForEntity(resultUrl, null, String.class);
        assertThat(wrongResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When another try to access card information.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> wrongResponse2 = basicAuthTemplate(javajigi).postForEntity(resultUrl, null,  String.class);
        assertThat(wrongResponse2.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void editDescription() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard7",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard7");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck6",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck6");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard5", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //when 'card owner' try to edit Description.
        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard5");
        String resultUrl = String.format("/api/boards/%d/%d/%d/editDescription", targetBoard.getId(), targetDeck.getId(), targetCard.getId());
        ResponseEntity<String> response4 = basicAuthTemplate().postForEntity(resultUrl, "edited Description", String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.OK));

        //when 'guest' try to edit Description.
        ResponseEntity<String> wrongResponse = template().postForEntity(resultUrl, "edited Description2", String.class);
        assertThat(wrongResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When another try to edit Description.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> wrongResponse2 = basicAuthTemplate(javajigi).postForEntity(resultUrl, "edited Description3",  String.class);
        assertThat(wrongResponse2.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void addComment() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard8",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard8");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck7",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck7");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard6", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //when 'card owner' try to add Comment.
        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard6");
        String resultUrl = String.format("/api/boards/%d/%d/%d/addComment", targetBoard.getId(), targetDeck.getId(), targetCard.getId());
        ResponseEntity<String> response4 = basicAuthTemplate().postForEntity(resultUrl, "new comment", String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.OK));

        //when 'guest' try to add Comment.
        ResponseEntity<String> wrongResponse = template().postForEntity(resultUrl, "new comment2", String.class);
        assertThat(wrongResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //When another try to add Comment.
        User javajigi = userRepository.findOne((long)1);
        ResponseEntity<String> wrongResponse2 = basicAuthTemplate(javajigi).postForEntity(resultUrl, "new comment3",  String.class);
        assertThat(wrongResponse2.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void editBoardTitle_success() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard9",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard9");
        String url = String.format("/api/boards/%d", targetBoard.getId());

        basicAuthTemplate().put(url, "edited title", String.class);
        ToDoBoard editedBoard = toDoBoardRepository.findOne(targetBoard.getId());
        assertEquals(editedBoard.getTitle(), "edited title");
    }

    @Test
    public void editBoardTitle_guest() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard10",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard10");
        String url = String.format("/api/boards/%d", targetBoard.getId());

        template().put(url, "edited title", String.class);
        ToDoBoard editedBoard = toDoBoardRepository.findOne(targetBoard.getId());
        assertEquals(editedBoard.getTitle(), "testBoard10");
    }

    @Test
    public void editBoardTitle_anotherUser() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard11",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard11");
        String url = String.format("/api/boards/%d", targetBoard.getId());

        User javajigi = userRepository.findOne((long)1);
        basicAuthTemplate(javajigi).put(url, "edited title", String.class);
        ToDoBoard editedBoard = toDoBoardRepository.findOne(targetBoard.getId());
        assertEquals(editedBoard.getTitle(), "testBoard11");
    }

    @Test
    public void editDeckTitle_success() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard12",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard12");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck8",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck8");
        String url2 = String.format("/api/boards/%d/%d", targetBoard.getId(), targetDeck.getId());

        basicAuthTemplate().put(url2, "edited title", String.class);
        ToDoDeck editedDeck = toDoDeckRepository.findOne(targetDeck.getId());
        assertEquals(editedDeck.getTitle(), "edited title");
    }

    @Test
    public void editDeckTitle_guest() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard13",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard13");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck9",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck9");
        String url2 = String.format("/api/boards/%d/%d", targetBoard.getId(), targetDeck.getId());

        template().put(url2, "edited title", String.class);
        ToDoDeck editedDeck = toDoDeckRepository.findOne(targetDeck.getId());
        assertEquals(editedDeck.getTitle(), "testDeck9");
    }

    @Test
    public void editDeckTitle_anotherUser() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard14",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard14");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck10",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck10");
        String url2 = String.format("/api/boards/%d/%d", targetBoard.getId(), targetDeck.getId());

        User javajigi = userRepository.findOne((long)1);
        basicAuthTemplate(javajigi).put(url2, "edited title", String.class);
        ToDoDeck editedDeck = toDoDeckRepository.findOne(targetDeck.getId());
        assertEquals(editedDeck.getTitle(), "testDeck10");
    }

    @Test
    public void editCardTitle_success() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard15",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard15");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck11",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck11");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard7", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard7");
        String url3 = String.format("/api/boards/%d/%d/%d", targetBoard.getId(), targetDeck.getId(), targetCard.getId());

        basicAuthTemplate().put(url3, "edited title", String.class);
        ToDoCard editedCard = toDoCardRepository.findOne(targetCard.getId());
        assertEquals(editedCard.getTitle(), "edited title");
    }

    @Test
    public void editCardTitle_guest() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard16",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard16");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck12",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck12");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard8", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard8");
        String url3 = String.format("/api/boards/%d/%d/%d", targetBoard.getId(), targetDeck.getId(), targetCard.getId());

        template().put(url3, "edited title", String.class);
        ToDoCard editedCard = toDoCardRepository.findOne(targetCard.getId());
        assertEquals(editedCard.getTitle(), "testCard8");
    }

    @Test
    public void editCardTitle_anotherUser() {
        //add board
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard17",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //add deck
        ToDoBoard targetBoard = toDoBoardRepository.findByTitle("testBoard17");
        String url = String.format("/api/boards/%d/add", targetBoard.getId());
        ResponseEntity<String> response2 = basicAuthTemplate().postForEntity(url, "testDeck13",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //add card
        ToDoDeck targetDeck = toDoDeckRepository.findByTitle("testDeck13");
        String url2 = String.format("/api/boards/%d/%d/add", targetBoard.getId(), targetDeck.getId());
        ResponseEntity<String> response3 = basicAuthTemplate().postForEntity(url2, "testCard9", String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        ToDoCard targetCard = toDoCardRepository.findByTitle("testCard9");
        String url3 = String.format("/api/boards/%d/%d/%d", targetBoard.getId(), targetDeck.getId(), targetCard.getId());

        User javajigi = userRepository.findOne((long)1);
        basicAuthTemplate(javajigi).put(url3, "edited title", String.class);
        ToDoCard editedCard = toDoCardRepository.findOne(targetCard.getId());
        assertEquals(editedCard.getTitle(), "testCard9");
    }

    @Test
    public void deleteBoard() {
    }

    @Test
    public void deleteDeck() {
    }

    @Test
    public void deleteCard() {
    }
}