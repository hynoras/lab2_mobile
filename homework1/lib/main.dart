import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(SentimentAnalysisApp());
}

class SentimentAnalysisApp extends StatefulWidget {
  @override
  _SentimentAnalysisAppState createState() => _SentimentAnalysisAppState();
}

class _SentimentAnalysisAppState extends State<SentimentAnalysisApp> {
  final TextEditingController _textEditingController = TextEditingController();
  String _sentiment = '';
  IconData _sentimentIcon = Icons.sentiment_neutral;
  Color _backgroundColor = Colors.white;

  Future<String> queryAPI(String text) async {
    const apiUrl = "https://api-inference.huggingface.co/models/wonrax/phobert-base-vietnamese-sentiment";
    final headers = {
      "Authorization": "Bearer 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",
      "Content-Type": "application/json",
    };

    final payload = jsonEncode({"inputs": text});
    final response = await http.post(Uri.parse(apiUrl), headers: headers, body: payload);

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body)[0] as List<dynamic>;
      String sentiment = '';
      double maxScore = 0.0;
      for (var item in data) {
        double score = item['score'];
        if (score > maxScore) {
          maxScore = score;
          sentiment = item['label'];
        }
      }
      return sentiment;
    } else {
      throw Exception('Failed to query API');
    }
  }

  void _analyzeSentiment() async {
    String inputText = _textEditingController.text;
    String sentiment;
    try {
      sentiment = await queryAPI(inputText);
    } catch (e) {
      sentiment = 'Error';
    }

    IconData sentimentIcon;
    Color backgroundColor;
    switch (sentiment) {
      case 'POS':
        sentimentIcon = Icons.sentiment_satisfied_alt;
        backgroundColor = Colors.green;
        break;
      case 'NEG':
        sentimentIcon = Icons.sentiment_dissatisfied;
        backgroundColor = Colors.red;
        break;
      case 'NEU':
        sentimentIcon = Icons.sentiment_neutral;
        backgroundColor = Colors.grey;
        break;
      default:
        sentimentIcon = Icons.sentiment_neutral;
        backgroundColor = Colors.grey;
    }

    setState(() {
      _sentiment = sentiment;
      _sentimentIcon = sentimentIcon;
      _backgroundColor = backgroundColor;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          
          title: const Text('Sentiment Analysis'),
        ),
        body: Container(
          color: _backgroundColor,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
              children: [
                TextField(
                  controller: _textEditingController,
                  decoration: const InputDecoration(
                    hintText: 'Say something...',
                    contentPadding: EdgeInsets.all(16.0),
                  ),
                ),
                const SizedBox(height: 20),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.purple,
                    foregroundColor: Colors.black,
                    shadowColor: Colors.black
                  ),
                  onPressed: _analyzeSentiment,
                  child: const Text('Submit'),
                ),
                const SizedBox(height: 20),
                Icon(
                  _sentimentIcon,
                  size: 100
                ),
              ],
          ),
        )

      ),
    );
  }
}
