package nextstep.subway.path;

import nextstep.subway.line.application.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PathFinderTest {
  Station 교대역;
  Station 강남역;
  Station 선릉역;
  Station 선정릉역;
  Station 고속터미널역;
  Station 신논현역;
  Station 양재역;

  Line 이호선;
  Line 삼호선;
  Line 구호선;
  Line 수인분당선;
  Line 신분당선;

  @BeforeEach
  void setUp() {
    교대역 = new Station(1L, "교대역");
    강남역 = new Station(2L, "강남역");
    선릉역 = new Station(3L, "선릉역");
    선정릉역 = new Station(4L, "선정릉역");
    고속터미널역 = new Station(5L, "고속터미널역");
    신논현역 = new Station(6L, "신논현역");
    양재역 = new Station(7L, "양재역");

    이호선 = new Line(1L, "이호선", "green", Section.of(1L, 교대역, 선릉역, Distance.of(50)));
    삼호선 = new Line(2L, "삼호선", "orange", Section.of(1L, 교대역, 고속터미널역, Distance.of(10)));
    구호선 = new Line(3L, "구호선", "brown", Section.of(1L, 고속터미널역, 선정릉역, Distance.of(50)));
    수인분당선 = new Line(4L, "수인분당선", "yellow", Section.of(1L, 선릉역, 선정릉역, Distance.of(10)));
    신분당선 = new Line(5L, "신분당선", "red", Section.of(1L, 강남역, 양재역, Distance.of(10)));
  }

  @DisplayName("최단 경로 조회")
  @Test
  void 최단_경로_조회() {
    // given
    이호선.addSection(Section.of(2L, 교대역, 강남역, Distance.of(30)));
    구호선.addSection(Section.of(2L, 고속터미널역, 신논현역, Distance.of(10)));

    List<Line> 노선_목록 = new ArrayList<>(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선));
    PathFinder 경로_탐색기 = PathFinder.of(노선_목록);

    // when
    List<Station> 최단_경로 = 경로_탐색기.findShortestPath(강남역, 신논현역);

    // then
    assertThat(최단_경로).containsExactly(강남역, 교대역, 고속터미널역, 신논현역);
  }

  @DisplayName("출발역과 도착역이 같은 경우")
  @Test
  void 동일한_출발역_도착역_예외() {
    // given
    List<Line> 노선_목록 = new ArrayList<>(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선));
    PathFinder 경로_탐색기 = PathFinder.of(노선_목록);

    // when
    Throwable thrown = catchThrowable(() -> 경로_탐색기.findShortestPath(강남역, 강남역));

    // that
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다. 입력 지하철역: " + 강남역.getName());
  }

  @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
  @Test
  void 존재하지_않은_역_최단_경로_조회_예외() {
    // given
    이호선.addSection(Section.of(2L, 교대역, 강남역, Distance.of(30)));
    List<Line> 노선_목록 = new ArrayList<>(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선));
    PathFinder 경로_탐색기 = PathFinder.of(노선_목록);

    // when
    Throwable thrown = catchThrowable(() -> 경로_탐색기.findShortestPath(강남역, 신논현역));

    // that
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(신논현역.getName() + ": 존재하지 않은 지하철 역입니다.");
  }

  @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
  @Test
  void 출발역_도착역_연결되지_않음_예외() {
    // given
    List<Line> 노선_목록 = new ArrayList<>(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선, 신분당선));
    PathFinder 경로_탐색기 = PathFinder.of(노선_목록);

    // when
    Throwable thrown = catchThrowable(() -> 경로_탐색기.findShortestPath(교대역, 양재역));

    // that
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
  }
}
